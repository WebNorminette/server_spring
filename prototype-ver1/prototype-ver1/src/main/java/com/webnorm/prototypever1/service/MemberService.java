package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.dto.request.address.AddressAddRequest;
import com.webnorm.prototypever1.dto.request.address.AddressUpdateRequest;
import com.webnorm.prototypever1.dto.request.member.MemberUpdateRequest;
import com.webnorm.prototypever1.entity.member.Address;
import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.entity.order.Order;
import com.webnorm.prototypever1.exception.exceptions.AddressException;
import com.webnorm.prototypever1.security.oauth.SocialType;
import com.webnorm.prototypever1.security.redis.RedisTokenInfo;
import com.webnorm.prototypever1.exception.exceptions.AuthException;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.MemberException;
import com.webnorm.prototypever1.repository.MemberRepository;
import com.webnorm.prototypever1.repository.RedisTokenInfoRepository;
import com.webnorm.prototypever1.security.provider.JwtTokenProvider;
import com.webnorm.prototypever1.security.TokenInfo;
import com.webnorm.prototypever1.util.DataPattern;
import com.webnorm.prototypever1.util.DataPatternMatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTokenInfoRepository redisTokenInfoRepository;

    /*
    * [회원가입]
    * # 아이디(이메일) 중복검사
    * # 비밀번호 인코딩
    * # 이메일 전송
    */
    public Member saveMember(Member member) {
        member.encodePassword(passwordEncoder);
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember.isPresent())
            throw new BusinessLogicException(MemberException.EMAIL_DUP);
        return memberRepository.save(member);
    }

    // 회원목록 조회(관리자)
    public Page<Member> findAllMember(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    // 회원 id로 조회
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));
    }

    /*
     * [로그인] : 일반 로그인(ORIGIN)
     * */
    public TokenInfo login(String memberId, String password) {
        // login id, pw 값을 넣어 Authentication 객체 생성 (authenticated = false)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(memberId, password);
        // 사용자 인증 (id로 사용자를 불러와 pw 체크)
        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authenticationToken);
        // 인증 결과를 넣어 atk 생성
        String accessToken = jwtTokenProvider
                .generateAccessToken(authentication, SocialType.ORIGIN, memberId);
        // rtk 생성
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        // redis 에 rtk, atk, memberId 세트로 저장
        RedisTokenInfo savedRedisTokenInfo = redisTokenInfoRepository.save(
                RedisTokenInfo.builder()
                        .id(memberId)
                        .refreshToken(refreshToken)
                        .accessToken(accessToken)
                        .build()
        );
        // TokenInfo 생성 후 리턴
        TokenInfo tokenInfo = TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType("Bearer")
                .build();
        return tokenInfo;
    }

    /*
     * [ATK 재발급]
     * RTK 를 받아 유효성 검증
     * 유효한 경우 새로운 ATK, RTK 리턴
     * */
    public TokenInfo reissueToken(String refreshToken, String accessToken) {
        // rtk 가 존재하고 rtk, atk 모두 유효한 경우
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)
                && jwtTokenProvider.validateToken(accessToken)) {
            // Authentication 객체 생성 (ATK 기반)
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            // Authentication 객체 기반으로 새 ATK 생성
            String newAccessToken = jwtTokenProvider
                    .regenerateAccessTokenByAccessToken(authentication, accessToken);
            // 새 RTK 생성
            String newRefreshToken = jwtTokenProvider.generateRefreshToken();
            TokenInfo tokenInfo = TokenInfo.builder()
                    .grantType("Bearer")
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
            return tokenInfo;
        } else
            throw new BusinessLogicException(AuthException.TOKEN_NOT_FOUND);
    }

    /*
     * [회원정보 수정]
     */
    public Member updateMember(String memberId, MemberUpdateRequest request) {
        // 사용자 id로 불러오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));

        // 형식 체크
        DataPatternMatcher.doesMatch(request.getName(), DataPattern.NAME);
        DataPatternMatcher.doesMatch(request.getEmail(), DataPattern.EMAIL);

        // email 중복체크 (email 변경 요청시에만 -> 기존 email 과 상이한 경우)
        if (!member.compWithOriginEmail(request.getEmail())) {
            // id 로 찾은 회원과 같은 socialType 을 가진 회원 중, 변경하려는 이메일(request.email) 과 같은 이메일을 중복으로 가진 회원 조회
            if (memberRepository.findByEmail(request.getEmail()).isPresent())
                throw new BusinessLogicException(MemberException.EMAIL_DUP);
        }

        Member updatedMember = member.update(       // 수정할 Member 객체 생성
                request.getName(),
                request.getEmail()
        );
        return memberRepository.save(updatedMember);
    }

    /*
     * [회원 비밀번호 수정]
     * */
    public Member updatePassword(String memberId, String newPassword) {
        // id 로 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));
        Member updatedMember = member.updatePassword(newPassword);  // 비밀번호 수정
        updatedMember.encodePassword(passwordEncoder);              // 인코딩
        return memberRepository.save(updatedMember);
    }

    /*
     * [회원 삭제/탈퇴]
     */
    public Member deleteMember(String memberId) {
        // id 로 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));

        memberRepository.delete(member);
        return member;
    }

    /*
     * [회원 주소 추가]
     * */
    public Member addAddress(String email, Address address) {
        // email 로 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));
        if (member.getDefaultAddress() == null)     // 기본 배송지가 없는 경우 -> 기본 배송지로 설정
            member.setDefaultAddress(address);
        Member updatedMember = member.addAddress(address);  // 주소 리스트에 추가
        return memberRepository.save(updatedMember);
    }

    /*
     * [기본 배송지 설정]
     * */
    public Member setDefaultAddress(String email, String addressId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));
        List<Address> addressList = member.getAddressList();
        Address findAddress = null;
        for (Address address : addressList) {
            if (address.getId().equals(addressId)) {
                findAddress = address;
                break;
            }
        }
        if (findAddress == null) throw new BusinessLogicException(AddressException.ADDRESS_NOT_FOUND);
        return memberRepository.save(member.setDefaultAddress(findAddress));
    }

    /*
     * [기본 배송지 조회]
     * */
    public Address findDefaultAddress(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));
        return member.getDefaultAddress();
    }

    /*
     * [주소 리스트 조회]
     * */
    public List<Address> findAllAddress(String email) {
        // email 로 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));
        List<Address> addressList = member.getAddressList();    // 주소 리스트 불러오기
        return addressList;
    }

    /*
     * [주소 수정]
     * */
    public Member updateAddress(String email, String addressId, AddressUpdateRequest request) {
        // email 로 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));
        Address addressToUpdate = request.toEntity();
        // 회원 기본 배송지에서 id 로 조회 후 수정
        Address defaultAddress = member.getDefaultAddress();
        if (defaultAddress.getId().equals(addressId))
            member.setDefaultAddress(defaultAddress.update(addressToUpdate));
        // 회원의 주소 리스트에서 id 로 주소 조회 후 수정
        List<Address> updatedAddressList = member.getAddressList().stream().map(address -> {
            if (address.getId().equals(addressId)) {
                return address.update(addressToUpdate);
            }
            return address;
        }).toList();
        return memberRepository.save(member.updateAddressList(updatedAddressList));
    }

    /*
     * [주소 삭제]
     * */
    public Member deleteAddress(String email, String addressId) {
        // email 로 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));
        // 주소 리스트에서 id 로 조회해 삭제 후 업데이트
        List<Address> addressList = member.getAddressList();
        List<Address> updatedAddressList = new ArrayList<>();
        for (Address address : addressList) {
            if (!address.getId().equals(addressId)) updatedAddressList.add(address);
        }
        // 기본 배송지를 삭제하는 경우
        Address defaultAddress = member.getDefaultAddress();
        if (defaultAddress.getId().equals(addressId))
            member.setDefaultAddress(updatedAddressList.get(0));    // 리스트 중 첫번째를 기본 배송지로 설정
        return memberRepository.save(member.updateAddressList(updatedAddressList));
    }
}

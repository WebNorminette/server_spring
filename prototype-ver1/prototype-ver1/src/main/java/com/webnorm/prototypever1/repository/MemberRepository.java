
package com.webnorm.prototypever1.repository;

import com.webnorm.prototypever1.entity.member.Address;
import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.security.oauth.SocialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {
    Page<Member> findAll(Pageable pageable);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndSocialType(String email, SocialType socialType);

}

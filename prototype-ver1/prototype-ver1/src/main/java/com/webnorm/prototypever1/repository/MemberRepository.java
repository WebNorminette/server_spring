package com.webnorm.prototypever1.repository;

import com.webnorm.prototypever1.entity.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {

    void findByUserId(String userId);
}

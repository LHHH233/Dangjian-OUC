package com.partymember.repository;

import com.partymember.entity.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {
    
    /**
     * 根据姓名和身份证号查询党员信息
     */
    Optional<PartyMember> findByNameAndIdCardNumber(String name, String idCardNumber);
    
    /**
     * 根据姓名模糊查询
     */
    List<PartyMember> findByNameContainingIgnoreCase(String name);
    
    /**
     * 根据身份证号查询
     */
    Optional<PartyMember> findByIdCardNumber(String idCardNumber);
    
    /**
     * 根据党组织查询
     */
    List<PartyMember> findByOrganization(String organization);
    
    /**
     * 根据政治面貌查询
     */
    List<PartyMember> findByPoliticalStatus(String politicalStatus);
    
    /**
     * 根据姓名和身份证号模糊查询
     */
    @Query("SELECT p FROM PartyMember p WHERE " +
           "(:name IS NULL OR p.name LIKE %:name%) AND " +
           "(:idCardNumber IS NULL OR p.idCardNumber LIKE %:idCardNumber%)")
    List<PartyMember> searchByNameAndIdCard(@Param("name") String name, 
                                           @Param("idCardNumber") String idCardNumber);
}


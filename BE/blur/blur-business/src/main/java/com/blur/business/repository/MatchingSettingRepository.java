package com.blur.business.repository;

import com.blur.business.entity.MatchingSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingSettingRepository extends JpaRepository<MatchingSetting, Integer> {

    MatchingSetting findByUserId(String userId);
}

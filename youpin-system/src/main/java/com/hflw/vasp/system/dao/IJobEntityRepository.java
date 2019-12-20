package com.hflw.vasp.system.dao;

import com.hflw.vasp.repository.BaseRepository;
import com.hflw.vasp.system.entity.JobEntity;

import java.io.Serializable;

public interface IJobEntityRepository extends BaseRepository<JobEntity, Long>, Serializable {
}

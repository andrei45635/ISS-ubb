package com.example.demo.repo.qas;

import com.example.demo.model.QA;
import com.example.demo.repo.IRepo;

public interface QARepo extends IRepo<Integer, QA> {
    QA findByUsernamePwd(String username, String password);
}

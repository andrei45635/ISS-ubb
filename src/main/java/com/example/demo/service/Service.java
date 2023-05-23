package com.example.demo.service;

import com.example.demo.model.Bug;
import com.example.demo.model.BugStatus;
import com.example.demo.model.Programmer;
import com.example.demo.model.QA;
import com.example.demo.repo.bugs.BugsRepo;
import com.example.demo.repo.bugs.BugsRepoDB;
import com.example.demo.repo.programmers.ProgrammersRepo;
import com.example.demo.repo.programmers.ProgrammersRepoDB;
import com.example.demo.repo.qas.QARepo;
import com.example.demo.repo.qas.QARepoDB;
import com.example.demo.utils.LoginResponse;
import com.example.demo.utils.LoginType;
import com.example.demo.utils.event.BugEntityChangeEvent;
import com.example.demo.utils.event.ChangeEventType;
import com.example.demo.utils.observer.Observable;
import com.example.demo.utils.observer.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Service implements Observable<BugEntityChangeEvent> {
    private BugsRepo bugsRepo;
    private ProgrammersRepo programmersRepo;
    private QARepo qaRepo;
    private List<Observer<BugEntityChangeEvent>> observers = new ArrayList<>();

    public Service(BugsRepo bugsRepo, ProgrammersRepo programmersRepo, QARepo qaRepo) {
        this.bugsRepo = bugsRepo;
        this.programmersRepo = programmersRepo;
        this.qaRepo = qaRepo;
    }

    public List<Bug> getAllBugs() {
        BugsRepoDB.initialize();
        List<Bug> bugs = bugsRepo.getAll();
        System.out.println("asdasda" + bugs);
        BugsRepoDB.close();
        return bugs;
    }

    public Programmer findProgrammerByUsernamePwd(String username, String pwd) {
        ProgrammersRepoDB.initialize();
        Programmer prog = programmersRepo.findByUsernamePwd(username, pwd);
        ProgrammersRepoDB.close();
        return prog;
    }

    public QA findQAByUsernamePwd(String username, String pwd) {
        QARepoDB.initialize();
        QA qa = qaRepo.findByUsernamePwd(username, pwd);
        System.out.println("asdasa" + qa);
        QARepoDB.close();
        return qa;
    }

    public LoginResponse login(String username, String password) {
        //LoginResponse loginResponse = new LoginResponse(LoginType.ERROR, (QA) null);
        LoginResponse loginResponse = new LoginResponse(LoginType.ERROR);
        QA loggedInQA = findQAByUsernamePwd(username, password);
        System.out.println(loggedInQA);
        if (loggedInQA != null) {
            loginResponse.setLoginType(LoginType.QA);
            loginResponse.setQa(loggedInQA);
        } else {
            Programmer loggedInProgrammer = findProgrammerByUsernamePwd(username, password);
            System.out.println(loggedInProgrammer);
            loginResponse.setLoginType(LoginType.PROGRAMMER);
            loginResponse.setProg(loggedInProgrammer);
        }
        return loginResponse;
    }

    public void save(String name, String description) throws IOException {
        Bug bug = new Bug(name, description, BugStatus.UNSOLVED);
        bugsRepo.save(bug);
        notifyObservers(new BugEntityChangeEvent(ChangeEventType.RECORD, bug));
    }

    public void modify(int id, String name, String description) throws IOException {
        bugsRepo.updateExperimental(id, name, description);
        notifyObservers(new BugEntityChangeEvent(ChangeEventType.MODIFY, new Bug(id, name, description, null)));
    }

    public void solveBug(int id){
        //bug.setStatus(BugStatus.SOLVED);
        bugsRepo.solveBug(id, BugStatus.SOLVED);
        notifyObservers(new BugEntityChangeEvent(ChangeEventType.SOLVE, new Bug(id, "random", "random", BugStatus.SOLVED)));
    }

    @Override
    public void addObserver(Observer<BugEntityChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<BugEntityChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(BugEntityChangeEvent t) {
        observers.forEach(x -> x.update(t));
    }
}

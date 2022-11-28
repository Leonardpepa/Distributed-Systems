package org.bank;

import io.grpc.*;
import org.bank.Model.AccountRepository;
import org.bank.Model.DatabaseConnector;
import org.bank.Model.StatementRepository;
import org.bank.grpc.Bank.BankGrpc;
import org.bank.grpc.BankServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
    public static ConcurrentHashMap<Integer, ReentrantReadWriteLock> locks;
    public static void main(String[] args) {
        locks = new ConcurrentHashMap<>();
        DatabaseConnector.Init_DB();
        initLocks(new AccountRepository(new DatabaseConnector().getConnection()));
        ServerBuilder<?> serverBuilder = Grpc.newServerBuilderForPort(5050, InsecureServerCredentials.create());;
        io.grpc.Server server = serverBuilder.addService(new BankServiceImpl()).build();

        try {
            System.out.println("Server started on port 5050");
            server.start();
            server.awaitTermination();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void putLock(int id){
        locks.putIfAbsent(id, new ReentrantReadWriteLock());
    }

    public static void twoFaceLock(int id_from, int id_to){
        if (!locks.containsKey(id_from) || !locks.containsKey(id_from)){
            throw new RuntimeException("Exception Lock object doesn't exist");
        }
        ReentrantReadWriteLock lockA;
        ReentrantReadWriteLock lockB;

        if (id_from < id_to){
            lockA = locks.get(id_from);
            lockB = locks.get(id_to);
        }else{
            lockA = locks.get(id_to);
            lockB = locks.get(id_from);
        }
        lockA.writeLock().lock();
        lockB.writeLock().lock();
    }

    public static void twoFaceUnlock(int id_from, int id_to){
        if (!locks.containsKey(id_from) || !locks.containsKey(id_from)){
            throw new RuntimeException("Exception Lock object doesn't exist");
        }
        ReentrantReadWriteLock lockA;
        ReentrantReadWriteLock lockB;

        if (id_from < id_to){
            lockA = locks.get(id_from);
            lockB = locks.get(id_to);
        }else{
            lockA = locks.get(id_to);
            lockB = locks.get(id_from);
        }
        lockA.writeLock().unlock();
        lockB.writeLock().unlock();
    }

    public static void lockRead(int id){
        if (locks.containsKey(id)){
            locks.get(id).readLock().lock();
        }else{
            throw new RuntimeException("Exception | Lock object is None for ID:" + id);
        }
    }
    public static void unlockRead(int id){
        if (locks.containsKey(id)){
            locks.get(id).readLock().unlock();
        }else{
            throw new RuntimeException("Exception | Lock object is None for ID:" + id);
        }
    }


    public static void lockWrite(int id){
        if (locks.containsKey(id)){
            locks.get(id).writeLock().lock();
        }else{
            throw new RuntimeException("Exception | Lock object is None for ID:" + id);
        }
    }
    public static void unlockWrite(int id){
        if (locks.containsKey(id)){
            locks.get(id).writeLock().unlock();
        }else{
            throw new RuntimeException("Exception | Lock object is None for ID:" + id);
        }
    }

    private static void initLocks(AccountRepository repo){
        for (Integer id: repo.getAllIds()){
            Server.putLock(id);
        }
        repo = null;
    }

}
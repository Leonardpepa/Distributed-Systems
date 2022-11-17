package Controller;

import Controller.ServerProtocolImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
    private static final String HOST = "localhost";
    private static final int PORT = Registry.REGISTRY_PORT;

    public static ConcurrentHashMap<Integer, ReentrantReadWriteLock> clientLocks;
    public Server() throws RemoteException {
        clientLocks = new ConcurrentHashMap<>();
        System.setProperty("java.rmi.server.hostname", HOST);
        ServerProtocolImpl serverApi = new ServerProtocolImpl();
        Registry registry = LocateRegistry.createRegistry(PORT);
        String remoteObjectName = "ATM_API";
        registry.rebind(remoteObjectName, serverApi);
        System.out.println("Controller.Server listening on port " + PORT);
    }
    public static void addClientLock(int id){
        clientLocks.putIfAbsent(id, new ReentrantReadWriteLock());
    }

    public static void lockRead(int id) {
        ReentrantReadWriteLock lock = clientLocks.get(id);
        if (lock == null) return;
        System.out.println("---------------");
        lock.readLock().lock();
        System.out.println("Account Id: " + id + " ReadLock: " + lock.getReadLockCount() + " WriteLock: " + lock.getWriteHoldCount());
        System.out.println("---------------");
    }

    public static void unlockRead(int id) {
        ReentrantReadWriteLock lock = clientLocks.get(id);
        if (lock == null) return;
        System.out.println("---------------");
        lock.readLock().unlock();
        System.out.println("Account Id: " + id + " ReadLock: " + lock.getReadLockCount() + " WriteLock: " + lock.getWriteHoldCount());
        System.out.println("---------------");
    }

    public static void lockWrite(int id) {
        ReentrantReadWriteLock lock = clientLocks.get(id);
        if (lock == null) return;
        System.out.println("---------------");
        lock.writeLock().lock();
        System.out.println("Account Id: " + id + " ReadLock: " + lock.getReadLockCount() + " WriteLock: " + lock.getWriteHoldCount());
        System.out.println("---------------");
    }

    public static void unlockWrite(int id) {
        ReentrantReadWriteLock lock = clientLocks.get(id);
        if (lock == null) return;
        System.out.println("---------------");
        lock.writeLock().unlock();
        System.out.println("Account Id: " + id + " ReadLock: " + lock.getReadLockCount() + " WriteLock: " + lock.getWriteHoldCount());
        System.out.println("---------------");
    }


}

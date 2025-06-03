//package com.example.demo;
//
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.locks.AbstractQueuedSynchronizer;
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReadWriteLock;
//
//public class ReentrantReadWriteLock implements ReadWriteLock, java.io.Serializable {
//    private final ReadLock readerLock;
//    private final WriteLock writerLock;
//    final Sync sync;
//
//    public ReentrantReadWriteLock() {
//        this(false);
//    }
//
//    public ReentrantReadWriteLock(boolean fair) {
//        sync = fair  new FairSync() : new NonfairSync();//这个是读写锁的内部类
//        readerLock = new ReadLock(this);
//        writerLock = new WriteLock(this);
//    }
//
////    static final class FairSync extends Sync {
////        private static final long serialVersionUID = -2274990926593161451L;
////        final boolean writerShouldBlock() {
////            return hasQueuedPredecessors();
////        }
////        final boolean readerShouldBlock() {
////            return hasQueuedPredecessors();
////        }
////    }
////
////    static final class NonfairSync extends Sync {
////        private static final long serialVersionUID = -8159625535654395037L;
////        final boolean writerShouldBlock() {
////            return false; // writers can always barge
////        }
////        final boolean readerShouldBlock() {
////            return apparentlyFirstQueuedIsExclusive();
////        }
////    }
////
////    final boolean apparentlyFirstQueuedIsExclusive() {
////        AbstractQueuedSynchronizer.Node h, s;
////        return (h = head) != null && (s = h.next)  != null &&
////                !(s instanceof AbstractQueuedSynchronizer.SharedNode) && s.waiter != null;
////    }
//
//    public ReadLock readLock() {
//        return readerLock;
//    }
//
//    public WriteLock writeLock() {
//        return writerLock;
//    }
//
//    abstract static class Sync extends AbstractQueuedSynchronizer {
//        // 读锁和写锁的实现细节
//    }
//
//    public static class ReadLock implements Lock, java.io.Serializable {
//        private final Sync sync;
//
//        protected ReadLock(ReentrantReadWriteLock lock) {
//            sync = lock.sync;
//        }
//
//        public void lock() {
//            sync.acquireShared(1);
//        }
//
//        @Override
//        public void lockInterruptibly() throws InterruptedException {
//
//        }
//
//        @Override
//        public boolean tryLock() {
//            return false;
//        }
//
//        @Override
//        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
//            return false;
//        }
//
//        public void unlock() {
//            sync.releaseShared(1);
//        }
//
//        @Override
//        public Condition newCondition() {
//            return null;
//        }
//    }
//
//    public static class WriteLock implements Lock, java.io.Serializable {
//        private final Sync sync;
//
//        protected WriteLock(ReentrantReadWriteLock lock) {
//            sync = lock.sync;
//        }
//
//        public void lock() {
//            sync.acquire(1);
//        }
//
//        @Override
//        public void lockInterruptibly() throws InterruptedException {
//
//        }
//
//        @Override
//        public boolean tryLock() {
//            return false;
//        }
//
//        @Override
//        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
//            return false;
//        }
//
//        public void unlock() {
//            sync.release(1);
//        }
//
//        @Override
//        public Condition newCondition() {
//            return null;
//        }
//    }
//}

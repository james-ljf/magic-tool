package com.magictool.web.util.lock;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 弱引用实现细粒度锁
 * @Author ljf
 * @Date 2022/9/23
 */
public class WeakRefHashLock {

    /**
     * 存储 key 对应 锁 的弱引用
     */
    private final ConcurrentHashMap<Object, LockWeakReference> lockMap = new ConcurrentHashMap<>();

    /**
     * 存储已过期的 ref
     */
    private final ReferenceQueue<ReentrantLock> queue = new ReferenceQueue<>();

    /**
     * 获取 key 对应的 lock
     */
    public Lock lock(Object key){

        if (lockMap.size() > 1000){
            clearEmptyRef();
        }

        // 获取 key 对应的 lock 弱引用
        LockWeakReference weakReference = lockMap.get(key);

        // 获取lock
        ReentrantLock lock = (weakReference == null ? null : weakReference.get());

        // 这里使用 while 循环获取，防止在获取过程中lock被gc回收
        while (lock == null){
            // 使用 putIfAbsent，在多线程环境下，针对同一 key ，weakReference 均指向同一弱引用对象
            // 这里使用 可重入锁
            weakReference = lockMap.putIfAbsent(key, new LockWeakReference(key, new ReentrantLock(), queue));

            // 获取弱引用指向的lock，这里如果获取到 lock 对象值，将会使 lock 对象值的弱引用提升为强引用，不会被gc回收
            lock = (weakReference == null ? null : weakReference.get());

            // 在 putIfAbsent 的执行和 weakReference.get() 执行的间隙，可能存在执行gc的过程，会导致 lock 为null，所以使用while循环获取
            if (lock != null){
                return lock;
            }

            // 获取不到 lock，移除map中无用的ref
            clearEmptyRef();

        }
        return lock;
    }

    /**
     * 清除 map 中已被回收的 ref
     */
    void clearEmptyRef(){

        Reference<? extends ReentrantLock> ref;
        while ((ref = queue.poll()) != null){
            LockWeakReference lockWeakReference = (LockWeakReference) ref;
            lockMap.remove(lockWeakReference.key);
        }

    }

    static class LockWeakReference extends WeakReference<ReentrantLock> {

        /**
         * 存储 弱引用 对应的 key 值，方便 之后的 remove 操作
         */
        private final Object key;

        public LockWeakReference(Object key, ReentrantLock lock, ReferenceQueue<? super ReentrantLock> q) {
            super(lock, q);
            this.key = key;
        }

    }

}

package edu.eci.arsw.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public final class PauseController {
  private final ReentrantLock lock = new ReentrantLock();
  private final Condition unpaused = lock.newCondition();
  private volatile boolean paused = false;
  private int pausedThreads = 0;
  private int totalThreads = 0;

  public void pause() { lock.lock(); try { paused = true; } finally { lock.unlock(); } }
  public void resume() { lock.lock(); try { paused = false; unpaused.signalAll(); } finally { lock.unlock(); } }
  public boolean paused() { return paused; }

  public void registerThread() {
    lock.lock();
    try {
      totalThreads++;
    } finally {
      lock.unlock();
    }
  }

  public boolean allPaused() {
    lock.lock();
    try {
      return paused && pausedThreads == totalThreads;
    } finally {
      lock.unlock();
    }
  }

  public void markPaused() {
    lock.lock();
    try {
      pausedThreads++;
    } finally {
      lock.unlock();
    }
  }

  public void markResumed() {
    lock.lock();
    try {
      if (pausedThreads > 0) pausedThreads--;
    } finally {
      lock.unlock();
    }
  }

  public void awaitIfPaused() throws InterruptedException {
    lock.lockInterruptibly();
    try { while (paused) unpaused.await(); }
    finally { lock.unlock(); }
  }

}

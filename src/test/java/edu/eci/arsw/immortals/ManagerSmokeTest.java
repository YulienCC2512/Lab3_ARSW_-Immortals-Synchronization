package edu.eci.arsw.immortals;

import org.junit.jupiter.api.Test;

final class ManagerSmokeTest {
  @Test
  void pauseAndResumeDemo() throws Exception {
    var m = new ImmortalManager(8, "ordered", 100, 10);
    m.start();
    Thread.sleep(50);
    m.pause();
    m.waitAllPaused(); // Espera a que todos los hilos estén realmente pausados
    // Verificar que todos los hilos están pausados
    boolean allPaused = m.populationSnapshot().stream().allMatch(im -> !im.isAlive() || im.isPaused());
    System.out.println("¿Todos los hilos pausados? " + allPaused);
    // Ahora reanudar y verificar que los hilos continúan
    m.resume();
    Thread.sleep(50);
    boolean anyPaused = m.populationSnapshot().stream().anyMatch(im -> im.isPaused());
    System.out.println("¿Algún hilo sigue pausado después de resume? " + anyPaused);
    m.stop();
  }
}

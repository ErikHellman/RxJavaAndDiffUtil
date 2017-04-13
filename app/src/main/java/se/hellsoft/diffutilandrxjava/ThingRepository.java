package se.hellsoft.diffutilandrxjava;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class ThingRepository {
  private static final int COUNT = 50;

  private static Random random = new Random();

  // Create a random list of Thing but with a consistent set of IDs.
  // Will just return a subset of the list to simulate removed items as well
  public static Flowable<List<Thing>> latestThings(long interval, TimeUnit timeUnit) {
    return Flowable
        .interval(0, interval, timeUnit, Schedulers.computation())
        .map(i -> shuffle(randomThings()).subList(0, (int) (COUNT * 0.8f)));
  }

  private static List<Thing> randomThings() {
    List<Thing> things = new ArrayList<>(COUNT);
    for (int i = 0; i < COUNT; i++) {
      Thing thing = newThing();
      thing.setId(i);
      things.add(thing);
    }
    return things;
  }

  private static List<Thing> shuffle(List<Thing> things) {
    List<Thing> shuffled = new ArrayList<>(things.size());
    while(!things.isEmpty()) {
      Thing thing = things.remove(random.nextInt(things.size()));
      shuffled.add(thing);
    }
    return shuffled;
  }

  private static Thing newThing() {
    Thing thing = new Thing();

    char first = (char) (random.nextInt(25) + 65);
    char second = (char) (random.nextInt(25) + 65);
    char third = (char) (random.nextInt(25) + 65);
    thing.setText(String.valueOf(new char[]{first, second, third}));

    int color = random.nextInt();
    thing.setColor(color);

    return thing;
  }
}

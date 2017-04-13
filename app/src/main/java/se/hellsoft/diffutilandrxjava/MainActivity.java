package se.hellsoft.diffutilandrxjava;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.computation;

public class MainActivity extends AppCompatActivity {
  private MyAdapter adapter;
  private Disposable disposable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onStart() {
    super.onStart();

    adapter = new MyAdapter();
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listOfThings);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(adapter);

    Flowable<List<Thing>> sharedThingsFlowable = ThingRepository
        .latestThings(2, TimeUnit.SECONDS)
        .onBackpressureBuffer()
        .share();

    Flowable<List<Thing>> startsWithEmptyList = sharedThingsFlowable.startWith(new ArrayList<Thing>());

    disposable = Flowable
        .zip(startsWithEmptyList, sharedThingsFlowable, (current, next) -> {
          MyDiffCallback diffCallback = new MyDiffCallback(current, next);
          DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback, true);
          return Pair.create(next, diffResult);
        })
        .subscribeOn(computation())
        .observeOn(mainThread())
        .subscribe(listDiffResultPair -> {
          adapter.setThings(listDiffResultPair.first);
          listDiffResultPair.second.dispatchUpdatesTo(adapter);
        });
  }

  @Override
  protected void onStop() {
    super.onStop();
    disposable.dispose();
  }

  private static class MyAdapter extends RecyclerView.Adapter<ThingViewHolder> {
    private List<Thing> things = new ArrayList<>(); // Start with empty list

    @Override
    public ThingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.thing_item, parent, false);
      return new ThingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThingViewHolder holder, int position) {
      Thing thing = things.get(position);
      holder.bind(thing);
    }

    @Override
    public int getItemCount() {
      return things.size();
    }

    public void setThings(List<Thing> things) {
      this.things = things;
    }
  }

  private static class ThingViewHolder extends RecyclerView.ViewHolder {

    private final TextView textView;

    public ThingViewHolder(View itemView) {
      super(itemView);
      textView = (TextView) itemView.findViewById(R.id.text);
    }

    public void bind(Thing thing) {
      itemView.setBackgroundColor(thing.getColor());
      textView.setText(thing.getText());
    }
  }

  private static class MyDiffCallback extends DiffUtil.Callback {
    private List<Thing> current;
    private List<Thing> next;

    public MyDiffCallback(List<Thing> current, List<Thing> next) {
      this.current = current;
      this.next = next;
    }

    @Override
    public int getOldListSize() {
      return current.size();
    }

    @Override
    public int getNewListSize() {
      return next.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
      Thing currentItem = current.get(oldItemPosition);
      Thing nextItem = next.get(newItemPosition);
      return currentItem.getId() == nextItem.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
      Thing currentItem = current.get(oldItemPosition);
      Thing nextItem = next.get(newItemPosition);
      return currentItem.equals(nextItem);
    }
  }
}

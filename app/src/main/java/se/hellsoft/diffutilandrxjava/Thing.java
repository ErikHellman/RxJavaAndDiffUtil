package se.hellsoft.diffutilandrxjava;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;

public class Thing implements Parcelable {
  private int id;
  private String text;
  @ColorInt
  private int color;

  private Thing() {
  }

  private Thing(int id, String text, int color) {
    this.id = id;
    this.text = text;
    this.color = color;
  }

  protected Thing(Parcel in) {
    id = in.readInt();
    text = in.readString();
    color = in.readInt();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final Creator<Thing> CREATOR = new Creator<Thing>() {
    @Override
    public Thing createFromParcel(Parcel in) {
      return new Thing(in);
    }

    @Override
    public Thing[] newArray(int size) {
      return new Thing[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(text);
    dest.writeInt(color);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public int getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  @ColorInt
  public int getColor() {
    return color;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Thing thing = (Thing) o;

    if (id != thing.id) return false;
    if (color != thing.color) return false;
    return text != null ? text.equals(thing.text) : thing.text == null;

  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (text != null ? text.hashCode() : 0);
    result = 31 * result + color;
    return result;
  }

  public static class Builder {
    private int id;
    private String text;
    private int color;

    private Builder() {
    }

    public Builder id(int id) {
      this.id = id;
      return this;
    }

    public Builder text(String text) {
      this.text = text;
      return this;
    }

    public Builder color(@ColorInt int color) {
      this.color = color;
      return this;
    }

    public Thing build() {
      return new Thing(id, text, color);
    }
  }
}

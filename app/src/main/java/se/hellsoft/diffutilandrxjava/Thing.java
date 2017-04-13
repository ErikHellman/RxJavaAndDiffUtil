package se.hellsoft.diffutilandrxjava;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;

public class Thing implements Parcelable {
  private int id;

  private String text;

  @ColorInt
  private int color;

  public Thing() {
  }

  public Thing(int  id, String text, int color) {
    this.id = id;
    this.text = text;
    this.color = color;
  }

  protected Thing(Parcel in) {
    id = in.readInt();
    text = in.readString();
    color = in.readInt();
  }

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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @ColorInt
  public int getColor() {
    return color;
  }

  public void setColor(@ColorInt int color) {
    this.color = color;
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
}

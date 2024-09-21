package it.unimol.viper.app;

public class Apple {
  private final int x;
  private final int y;
  private final AppleType type;

  public Apple(int x, int y, AppleType type) {
    this.x = x;
    this.y = y;
    this.type = type;
  }

  public int getX() { return x; }

  public int getY() { return y; }

  public AppleType getType() { return type; }
  public enum AppleType { NORMAL, BAD, GOLDEN, PINK }
}

package webprotegebackup;

public interface Storage {
  void save(String fileName, byte[] fileContent);
}

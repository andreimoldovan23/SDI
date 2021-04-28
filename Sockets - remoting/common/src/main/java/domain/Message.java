package domain;

import lombok.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class Message {
    public static final String OK = "ok";
    public static final String ERROR = "error";

    private String header;
    private final List<Object> body = new ArrayList<>();

    public void readFrom(InputStream is) throws IOException, ClassNotFoundException {
        var br = new ObjectInputStream(is);
        header = (String) br.readObject();
        Integer size = (Integer) br.readObject();
        body.clear();
        IntStream.range(0, size).forEach(i -> {
            try {
                body.add(br.readObject());
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Problem reading message");
            }
        });
    }

    public void writeTo(OutputStream os) throws IOException {
        var obs = new ObjectOutputStream(os);
        obs.writeObject(header);
        obs.writeObject(body.size());
        for (var o : body)
            obs.writeObject(o);
    }

}
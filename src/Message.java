public class Message<T> {
    private T message;

    public Message() {
        message = null;
    }

    public Message(T t) {
        message = t;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    public T getMessage() {
        return message;
    }
}

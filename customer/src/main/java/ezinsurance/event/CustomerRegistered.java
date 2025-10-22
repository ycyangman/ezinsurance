package ezinsurance.event;

public class CustomerRegistered extends AbstractEvent {

    private Long id;

    public CustomerRegistered(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

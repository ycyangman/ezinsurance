package ezinsurance.event;

public class CustomerMofied extends AbstractEvent {

    private Long id;

    public CustomerMofied(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

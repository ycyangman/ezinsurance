package ezinsurance.event;

public class PaymentRquested extends AbstractEvent {

    private Long id;

    public PaymentRquested(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

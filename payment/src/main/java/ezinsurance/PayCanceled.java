package ezinsurance;

public class PayCanceled extends AbstractEvent {

    private Long id;

    public PayCanceled(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

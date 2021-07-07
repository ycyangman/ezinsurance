package ezinsurance.event;

public class ProductDesdRequested extends AbstractEvent {

    private Long id;

    public ProductDesdRequested(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

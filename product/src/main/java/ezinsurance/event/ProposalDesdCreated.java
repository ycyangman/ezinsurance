package ezinsurance.event;

public class ProposalDesdCreated extends AbstractEvent {

    private Long id;

    public ProposalDesdCreated(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

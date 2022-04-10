package ija.app.uml;

public class UMLRelation {
    private String type;
    private String from;
    private String to;
    private String cardinalityFrom;
    private String cardinalityTo;

    public UMLRelation(String type, String from, String to, String cardinalityFrom, String cardinalityTo){
		this.type = type;
		this.from = from;
		this.to = to;
        this.cardinalityFrom = cardinalityFrom;
        this.cardinalityTo = cardinalityTo;
	}

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getFrom(){
        return from;
    }

    public void setFrom(String from){
        this.from = from;
    }

    public String getTo(){
        return to;
    }

    public void setTo(String to){
        this.to = to;
    }

    public String getCardinalityFrom(){
        return cardinalityFrom;
    }

    public void setCardinalityFrom(String cardinalityFrom){
        this.cardinalityFrom = cardinalityFrom;
    }

    public String getCardinalityTo(){
        return cardinalityTo;
    }

    public void setCardinalityTo(String cardinalityTo){
        this.cardinalityTo = cardinalityTo;
    }

	
}

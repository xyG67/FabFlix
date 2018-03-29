package cs122B.FabFlix.Model;

public class Stars {
	private String id;
	private String name;
	private String birthyear;
	
	public Stars(){
		this.id = null;
		this.name = null;
		this.birthyear = null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthyear() {
		return birthyear;
	}

	public void setBirthyear(String birthyear) {
		this.birthyear = birthyear;
	}
	
	
}

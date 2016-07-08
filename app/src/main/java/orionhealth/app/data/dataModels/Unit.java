package orionhealth.app.data.dataModels;

/**
 * Created by bill on 6/06/16.
 */
public enum Unit {
	MG("mg"),
	ML("ml"),
	SPRAY("spray"),
	TABLET("tablet");

	private final String name;

	Unit(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}


	@Override
	public String toString(){
		return getName();
	}

}

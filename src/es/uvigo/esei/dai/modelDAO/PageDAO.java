package es.uvigo.esei.dai.modelDAO;

public interface PageDAO {
	public void create(String uuid, String content); // Debe asignársele a employee el id generado
	public void update(String uuid, String content) throws PageNotFoundException;
	public void delete(String uuid) throws PageNotFoundException;
	public String get(String uuid) throws PageNotFoundException;
	public boolean containsKey(String uuid);
	public int size();
	public String getListUUID();
}

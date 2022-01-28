package br.com.bean;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.com.dao.DaoGeneric;
import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;
import br.com.repository.IDaoPessoa;
import net.bootsfaces.component.selectOneMenu.SelectOneMenu;

@javax.faces.view.ViewScoped
@Named(value = "pessoaBean")
public class PessoaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa pessoa = new Pessoa();

	@Inject
	private DaoGeneric<Pessoa> daoGeneric;

	private List<Pessoa> pessoas = new ArrayList<Pessoa>();

	@Inject
	private IDaoPessoa iDaoPessoa;
	private List<SelectItem> estados;
	private List<SelectItem> cidades;
	private Part arquivoFoto;

	@Inject
	private JPAUtil jpaUtil;

	public String salvar() throws Exception {

//		if (arquivoFoto.getInputStream() != null) {
//
//			// Processamento da imagem
//			byte[] imagemByte = getByte(arquivoFoto.getInputStream());
//
//			// ransforma em bufferimage
//			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByte));
//
//			if (bufferedImage != null) {
//				pessoa.setFotoIconBase64Original(imagemByte); // Salva imagem original
//				// Pega o tipo da imagem
//				int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
//
//				int largura = 200;
//				int altura = 200;
//
//				// Criar a miniatura
//				BufferedImage resizedImage = new BufferedImage(largura, altura, type);
//				Graphics2D g = resizedImage.createGraphics();
//				g.drawImage(bufferedImage, 0, 0, largura, altura, null);
//				g.dispose();
//
//				// Escrever novamente a imagem em tamanho menor
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				String extensao = arquivoFoto.getContentType().split("\\/")[1]; // image/png
//				ImageIO.write(resizedImage, extensao, baos);
//
//				String miniImagem = "data:" + arquivoFoto.getContentType() + ";base64,"
//						+ DatatypeConverter.printBase64Binary(baos.toByteArray());
//
//				// Fim do Processamento da imagem
//				pessoa.setFotoIconBase64(miniImagem);
//				pessoa.setExtensao(extensao);
//			}
//		}
		pessoa = daoGeneric.merge(pessoa);
		listaPessoa();
		mostrarMsg("Cadastrado com Sucesso!");
		return "";

	}

	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);

	}

	public String novo() {
		pessoa = new Pessoa();
		return "";
	}

	public String limpar() {
		pessoa = new Pessoa();
		return "";
	}

	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {

			URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/"); // pega o cep inserido e monta
																							// a url

			URLConnection connection = url.openConnection(); // abre uma conexão
			InputStream iStream = connection.getInputStream(); // executa no servidor e retorna os dados
			BufferedReader br = new BufferedReader(new java.io.InputStreamReader(iStream, "UTF-8")); // armazena os
																										// dados e faz a
																										// leitura

			String cep = "";
			StringBuilder jsonCep = new StringBuilder();

			while ((cep = br.readLine()) != null) { // o br está atribuindo as linhas ao cep, enquanto as linhas são
													// diferentes de nulo
				jsonCep.append(cep);
			}

			Pessoa gson = new Gson().fromJson(jsonCep.toString(), Pessoa.class);

			pessoa.setCep(gson.getCep());
			pessoa.setLogradouro(gson.getLogradouro());
			pessoa.setComplemento(gson.getComplemento());
			pessoa.setBairro(gson.getBairro());
			pessoa.setCidade(gson.getCidade());
			pessoa.setUf(gson.getUf());

		} catch (Exception e) {
			e.printStackTrace(); // imprime o erro no console
			mostrarMsg("Erro ao consultar o CEP");
		}
		
//		<h:outputLabel value="CEP:" />
//		<h:inputText value="#{pessoaBean.pessoa.cep}"
//			validatorMessage="CEP inválido" pt:placeholder="CEP" maxlength="8">
//			<f:ajax event="blur" execute="@this"
//				listener="#{pessoaBean.pesquisaCep}"
//				render="logradouro complemento bairro cidade uf" />
//			<!--Está enviando o CEP por objeto e depois execultando o método la no Bean -->
//			<f:validateLength minimum="8" />
//		</h:inputText>
	}

	public String delete() {
		daoGeneric.delete(pessoa);
		pessoa = new Pessoa();
		listaPessoa();
		mostrarMsg("Excluido com Sucesso!");
		return "";
	}

	public String logar() {

		Pessoa pessoaUser = iDaoPessoa.consutarUsurio(pessoa.getLogin(), pessoa.getSenha());

		if (pessoaUser != null) {

			FacesContext context = FacesContext.getCurrentInstance(); // é o cara responsável por toda criação e
																		// genciamento dos ciclos de vida da aplicação
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuariologado", pessoaUser);

			return "cadastro.jsf";
		} else {
			FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Usuário não encontrado"));
		}

		return "index.jsf";
	}

	public String deslogar() { // invalida a sessão
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuariologado");

		HttpServletRequest httpServletRequest = (HttpServletRequest) context.getCurrentInstance().getExternalContext()
				.getRequest();
		httpServletRequest.getSession().invalidate();
		return "index.jsf";

	}

	public boolean permiteAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuariologado");

		return pessoaUser.getPerfil().equals(acesso);
	}

	public void listaCidades(AjaxBehaviorEvent event) {

		Estados estado = (Estados) ((org.primefaces.component.selectonemenu.SelectOneMenu) event.getSource()).getValue();

		if (estado != null) {
			pessoa.setEstados(estado);

			List<Cidades> cidades = jpaUtil.getEntityManager()
					.createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();

			List<SelectItem> selectItemsCidades = new ArrayList<SelectItem>();

			for (Cidades cidade : cidades) {
				selectItemsCidades.add(new SelectItem(cidade, cidade.getNome()));

			}

			setCidades(selectItemsCidades);

		}
	}

	public String editar() {
		if (pessoa.getCidade() != null) {
			Estados estado = pessoa.getCidades().getEstados();
			pessoa.setEstados(estado);

			List<Cidades> cidades = jpaUtil.getEntityManager()
					.createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();

			List<SelectItem> selectItemsCidades = new ArrayList<SelectItem>();

			for (Cidades cidade : cidades) {
				selectItemsCidades.add(new SelectItem(cidade, cidade.getNome()));

			}

			setCidades(selectItemsCidades);
		}

		return "";
	}

	// Método que converte inputStream para array de bytes
	private byte[] getByte(InputStream is) throws IOException {
		int len;
		int size = 1024;
		byte[] buf = null;
		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);

		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];

			while ((len = is.read(buf, 0, size)) != -1) {
				bos.write(buf, 0, len);
			}
			buf = bos.toByteArray();
		}

		return buf;
	}

	public void download() throws IOException {
		Map<String, String> parms = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileDownloadId = parms.get("fileDownloadId");

		Pessoa pessoa = daoGeneric.consultar(Pessoa.class, fileDownloadId);

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();

		response.addHeader("Content-Disposition", "attachment; filename=download." + pessoa.getExtensao());
		response.setContentType("application/octet-stram");
		response.setContentLength(pessoa.getFotoIconBase64Original().length);
		response.getOutputStream().write(pessoa.getFotoIconBase64Original());
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
	}

	public List<SelectItem> getCidades() {
		return cidades;
	}

	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}

	@PostConstruct
	public void listaPessoa() {
		pessoas = daoGeneric.listaEntity(Pessoa.class);
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public List<SelectItem> getEstados() {
		estados = iDaoPessoa.listaEstados();
		return estados;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DaoGeneric<Pessoa> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public Part getArquivoFoto() {
		return arquivoFoto;
	}

	public void setArquivoFoto(Part arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}

}

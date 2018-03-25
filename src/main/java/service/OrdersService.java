package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import model.Article;
import model.Orders;

@Service
public class OrdersService {

	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private ProcessService processService;


	public List<Orders> getOrders(){
		
		String sql = "select top 50 IDZestaw, NAZWA, Ilosc, Opis, Artykul, 1 AS Partia , Kkod, IdxKatalogowy, NazwaCD, Numer "
				+ "from (select idZestaw,Ilosc,Opis, Numer "
				+ "from zestaw LEFT OUTER JOIN zleceniePrd on ZlMag = IDZlecenie where IDZestaw in (select zlecenie from partia where IDPartia in "
				+ "(select Partia "
				+ "from produkcja where idProdukcja in (select produkcja from timing where Koniec is null)))) "
				+ "zlecenia, towar, artykul where zlecenia.IDZestaw = Zrodlo AND Artykul = IDArtykul AND (Numer LIKE '%SZW%' or NAZWA LIKE '%PAS%') "
				+ "UNION ALL select IDZestaw, NAZWA, Ilosc, Opis, Artykul, NULL, Kkod, IdxKatalogowy, NazwaCD, Numer "
				+ "from zestaw LEFT OUTER JOIN zleceniePrd on ZlMag = IDZlecenie , towar, artykul WHERE IDZestaw = Zrodlo AND Artykul = IDArtykul AND IDZestaw "
				+ "NOT IN (SELECT zlecenie from partia) AND (Numer LIKE '%SZW%' or NAZWA LIKE '%PAS%') order by idZestaw desc";
		
		
		List<Orders> resultList = jdbc.query(sql, new RowMapper<Orders>() {

			public Orders mapRow(ResultSet result, int arg1) throws SQLException {
					Orders order = new Orders();
					order.setOrderId(result.getInt(1));//numer zlecenia
					order.setAmount(result.getInt(3));//ilosc do wykonania
					order.setOrderInProgress(result.getInt(6));//oznaczenie czy jest w trakcie wykonywania
					order.setNumber(result.getString(10));//numer z widoku zleceniePrd
					
					Article article = new Article();
					article.setId(result.getInt(5));//id artykul
					article.setKatalogIdx(result.getString(8));//indeks katalogowy
					article.setName(result.getString(2));//nazwa
					article.setNameCont(result.getString(9));//kontynuacja nazwy
					article.setBarCode(result.getString(7));//kod kreskowy
					article.setDescription(result.getString(4));//opis
					article.setProcessList(processService.getByArt(article.getId()));
					order.setArticle(article);

					return order;
			}
			
		});
		return resultList;
		
	}
	
	public List<Orders> getInProgressOrders(){
		List<Orders> orders = getOrders();
		List<Orders> list = new ArrayList<Orders>();
		for(Orders o : orders)
			if(o.getOrderInProgress() == 1)
				list.add(o);
		return list;
	}
	
	String sql = "";
}

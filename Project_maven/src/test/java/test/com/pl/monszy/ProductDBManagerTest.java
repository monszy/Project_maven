package test.com.pl.monszy;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.pl.monszy.PriceException;
import com.pl.monszy.Product;
import com.pl.monszy.ProductType;
import com.pl.services.ProductDBManager;

public class ProductDBManagerTest {

	ProductDBManager dbproduct = new ProductDBManager();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		
	}

	@Before
	public void setUp() throws Exception {
		
		dbproduct.addProduct(new Product("Fuji", "Best of the best", ProductType.Camera, 120));
	}

	@After
	public void tearDown() throws Exception {
		dbproduct.deleteAllProduct();
	}

	

	@Test
	public void testAddProduct() throws PriceException {
		dbproduct.addProduct(new Product("kojiak", "Bether ?", ProductType.Camera, 100));
		dbproduct.addProduct(new Product("Fuji", "Best of", ProductType.Film, 20));
		assertEquals(3, dbproduct.getAllProducts().size());
	}

	@Test
	public void testGetAllProducts() throws PriceException {
		dbproduct.addProduct(new Product("Fuji", "Best of", ProductType.Film, 20));
		assertEquals(2, dbproduct.getAllProducts().size());
	}

	@Test
	public void testDeleteAllProduct() throws PriceException {
		dbproduct.addProduct(new Product("Fuji", "Best of", ProductType.Film, 20));
		dbproduct.deleteAllProduct();
		assertEquals(0, dbproduct.getAllProducts().size());
		assertTrue(dbproduct.getAllProducts().size() == 0);
	}

	@Test
	public void testFindProductByName() throws PriceException {
		dbproduct.addProduct(new Product("kojiak", "Bether ?", ProductType.Camera, 100));
		dbproduct.addProduct(new Product("Fuji", "Best of", ProductType.Film, 20));
		assertEquals(1, dbproduct.findProductByName("kojiak").size());
	}

	@Test
	public void testDeleteProduct() throws PriceException {
		dbproduct.addProduct(new Product("kojiak", "Bether ?", ProductType.Camera, 100));
		dbproduct.addProduct(new Product("Fuji", "Best of", ProductType.Film, 20));
		dbproduct.deleteProduct(dbproduct.findProductByName("kojiak"));
		assertEquals(2, dbproduct.getAllProducts().size());
	}

	

}

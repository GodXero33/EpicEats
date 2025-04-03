import edu.icet.ecom.Main;
import edu.icet.ecom.service.custom.inventory.SupplierService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Main.class)
class SupplierServiceTest {
	@Autowired
	SupplierService supplierService;

	@Test
	void testGet () {
		Assertions.assertNotNull(this.supplierService.get(1L));
		Assertions.assertEquals(1l, this.supplierService.get(1L).getData().getId());
	}
}

package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.repository.RepairOrderRepository;
import org.infiniteam.autoservice.service.impl.RepairOrderServiceJpa;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class RepairOrderServiceTest {

    @Mock
    RepairOrderRepository repairOrderRepository;

    @InjectMocks
    RepairOrderService repairOrderService = new RepairOrderServiceJpa();

    RepairingRepairOrder repRo;

    Product product;

    @Before
    public void setup() {
        repRo = new RepairingRepairOrder();
        repRo.setPrice(0.0);
        repRo.setVehicle(new Vehicle());
        repRo.setServiceJobStatus(ServiceJobStatus.IN_PROGRESS);
        repRo.setAutoService(new AutoService());
        repRo.setCreationTime(LocalDateTime.now());

        product = new Product() {
            public String getName() {
                return "Item";
            }
            public double getPrice() {
                return 100.00;
            }
        };
    }


    @Test
    public void addItemToOrderAddsItemAndUpdatesPrice() {
        repairOrderService.addItemToOrder(repRo, product);

        assertEquals(100.00, (double) repRo.getPrice());
        assertThat(repRo.getItems()).hasSize(1);
        assertThat(repRo.getItems().get(0).getName()).isEqualTo("Item");
    }

    @Test
    public void addItemToOrderShouldThrowOnClosedRo() {
        repRo.setServiceJobStatus(ServiceJobStatus.FINISHED);

        assertThrows(RuntimeException.class, () -> repairOrderService.addItemToOrder(repRo, product));
    }

    @Test
    public void addItemToOrderShouldNotThrowOnFreeProduct() {
        product = new Product() {
            public String getName() {
                return "Free";
            }public double getPrice() {
                return 0.0;
            }
        };

        assertDoesNotThrow(() -> repairOrderService.addItemToOrder(repRo, product));
    }


}

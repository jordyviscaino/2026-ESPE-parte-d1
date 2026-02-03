package es.upm.grise.profundizacion.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import es.upm.grise.exceptions.IncorrectItemException;

public class OrderTest {

    private Order order;
    private Item mockItem;
    private Product mockProduct;

    @BeforeEach
    public void setUp() {
        order = new Order();
        mockItem = mock(Item.class);
        mockProduct = mock(Product.class);
    }

    // 1. Verificar que un item válido se añade correctamente
    @Test
    public void testAddItem_Success() throws IncorrectItemException {
        when(mockItem.getPrice()).thenReturn(10.0);
        when(mockItem.getQuantity()).thenReturn(2);
        when(mockItem.getProduct()).thenReturn(mockProduct);

        order.addItem(mockItem);

        assertEquals(1, order.getItems().size(), "La lista debería tener 1 item");
        assertTrue(order.getItems().contains(mockItem));
    }

    // 2. Verificar excepción por precio negativo
    @Test
    public void testAddItem_NegativePrice_ThrowsException() {
        when(mockItem.getPrice()).thenReturn(-1.0);
        
        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(mockItem);
        });
    }

    // 3. Verificar excepción por cantidad cero
    @Test
    public void testAddItem_ZeroQuantity_ThrowsException() {
        when(mockItem.getPrice()).thenReturn(10.0);
        when(mockItem.getQuantity()).thenReturn(0);
        
        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(mockItem);
        });
    }

    // 4. Verificar que se incrementa la cantidad si el producto y precio son iguales
    @Test
    public void testAddItem_DuplicateProductSamePrice_IncrementsQuantity() throws IncorrectItemException {
        Item existingItem = mock(Item.class);
        when(existingItem.getProduct()).thenReturn(mockProduct);
        when(existingItem.getPrice()).thenReturn(10.0);
        when(existingItem.getQuantity()).thenReturn(1);

        Item newItem = mock(Item.class);
        when(newItem.getProduct()).thenReturn(mockProduct);
        when(newItem.getPrice()).thenReturn(10.0);
        when(newItem.getQuantity()).thenReturn(3);

        order.addItem(existingItem);
        order.addItem(newItem);

        assertEquals(1, order.getItems().size(), "No debería añadir un nuevo item");
        verify(existingItem).setQuantity(4); // 1 + 3
    }

    // 5. Verificar que se añade nuevo item si el precio es distinto aunque sea el mismo producto
    @Test
    public void testAddItem_SameProductDifferentPrice_AddsNewItem() throws IncorrectItemException {
        Item item1 = mock(Item.class);
        when(item1.getProduct()).thenReturn(mockProduct);
        when(item1.getPrice()).thenReturn(10.0);
        when(item1.getQuantity()).thenReturn(1);

        Item item2 = mock(Item.class);
        when(item2.getProduct()).thenReturn(mockProduct);
        when(item2.getPrice()).thenReturn(15.0);
        when(item2.getQuantity()).thenReturn(1);

        order.addItem(item1);
        order.addItem(item2);

        assertEquals(2, order.getItems().size(), "Debería haber 2 items por tener precios distintos");
    }
}
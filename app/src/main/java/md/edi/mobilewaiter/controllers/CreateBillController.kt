package md.edi.mobilewaiter.controllers

import md.edi.mobilewaiter.data.remote.models.bill.AddOrders
import md.edi.mobilewaiter.data.remote.models.bill.OrderItem
import md.edi.mobilewaiter.data.remote.response.bills.BillItem
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*


object CreateBillController {
    var orderModel = AddOrders()
    private var countCart = 0.0
    val countCartFlow = MutableStateFlow(0.0)

    fun clearAllData() {
        countCart = 0.0
        countCartFlow.value = 0.0
        orderModel = AddOrders()
    }

    fun setTableId(id: String) {
        orderModel.TableUid = id
    }
    fun setTableGuests(number: Int) {
        orderModel.Guests = number
    }

    fun editBill(bill: BillItem) {
        orderModel = AddOrders()
        orderModel.BillUid = bill.Uid
        orderModel.TableUid = bill.TableUid
        orderModel.Guests = bill.Guests

        val listLines = mutableListOf<OrderItem>()
        var countProducts = 0.0
        bill.Lines.forEach {
            countProducts += it.Count
            listLines.add(
                OrderItem(
                    assortimentUid = it.AssortimentUid,
                    count = it.Count,
                    price = it.Sum / it.Count,
                    priceLineUid = it.PriceLineUid,
                    uId = it.Uid,
                    kitUid = it.KitUid,
                    comments = it.Comments ?: emptyList(),
                    sum = it.Sum,
                    sumAfterDiscount = it.SumAfterDiscount
                )
            )
        }
        setCartCount(countProducts)
        orderModel.Orders = listLines
    }

    fun changeTableBill(bill: BillItem, tableId: String) {
        orderModel = AddOrders()
        orderModel.BillUid = bill.Uid
        orderModel.TableUid = bill.TableUid
        orderModel.Guests = bill.Guests

        val listLines = mutableListOf<OrderItem>()
        var countProducts = 0.0
        bill.Lines.forEach {
            countProducts += it.Count
            listLines.add(
                OrderItem(
                    assortimentUid = it.AssortimentUid,
                    count = it.Count,
                    price = it.Sum / it.Count,
                    priceLineUid = it.PriceLineUid,
                    uId = it.Uid,
                    kitUid = it.KitUid,
                    comments = it.Comments ?: emptyList(),
                    sum = it.Sum,
                    sumAfterDiscount = it.SumAfterDiscount
                )
            )
        }
        orderModel.Orders = listLines
    }

    fun addAssortment(priceLineId: String, assortmentId: String, comments: List<String>, numberCook: Int, count: Double, price: Double) {
        val orderItem = OrderItem(
            assortimentUid = assortmentId,
            count = count,
            price = price,
            priceLineUid = priceLineId,
            comments = comments,
            numberPrepare = numberCook,
            sum = count * price,
            sumAfterDiscount = count * price,
            internUid = UUID.randomUUID().toString()
        )
        val ordersList = orderModel.Orders.toMutableList()
        ordersList.add(orderItem)
        orderModel.Orders = ordersList
    }

    fun getInternLineById(id: String) = orderModel.Orders.find { it.internUid == id }

    fun removeLineByInternId(id: String) {
        orderModel.Orders.find { it.internUid == id }?.let {
            val orderLines = orderModel.Orders.toMutableList()
            orderLines.remove(it)
            setCartCount(it.count * -1)
            orderModel.Orders = orderLines
        }

    }

    fun setCartCount(count: Double) {
        countCart += count
        countCartFlow.value = countCart
    }
}
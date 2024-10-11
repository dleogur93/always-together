package projlee.modules.order.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;
import projlee.modules.account.domain.Account;
import projlee.modules.account.domain.QAccount;
import projlee.modules.order.domain.DeliveryStatus;
import projlee.modules.order.domain.Order;
import projlee.modules.order.domain.OrderStatus;
import projlee.modules.order.domain.QOrder;
import projlee.modules.order.form.OrderSearch;
import projlee.modules.reservation.domain.QDogReservation;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;


public class OrderRepositoryImpl extends QuerydslRepositorySupport implements OrderRepositoryCustom {


    public OrderRepositoryImpl() {
        super(Order.class);
    }

    @Override
    public Page<Order> findSearchOrder(Pageable pageable, OrderSearch orderSearch) {

        QOrder order = QOrder.order;
        QAccount account = QAccount.account;

        JPQLQuery<Order> query =
                from(order)
                .join(order.account, account)
                .where(
                        statusEq(orderSearch.getOrderStatus()),
                        nameLike(orderSearch.getAccountName())
                )
                .orderBy(order.orderDateTime.desc())  // 정렬을 추가
                .distinct();


        JPQLQuery<Order> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Order> fetchResults =  pageableQuery.fetchResults();


        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }



    private BooleanExpression nameLike(String accountName) {
        if (!StringUtils.hasText(accountName)){
            return null;
        }
        return QAccount.account.name.like(accountName);
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {

        if (statusCond == null) {
            return null;
        }
        return QOrder.order.status.eq(statusCond);
    }

    @Override
    public Page<Order> accountCancelOrder(Pageable pageable, Account account) {

        QOrder order = QOrder.order;
        QAccount qAccount = QAccount.account;

        JPQLQuery<Order>  query =
                from(order)
                .join(order.account, qAccount).fetchJoin()
                .where(
                        order.status.eq(OrderStatus.CANCEL)
                        )
                .orderBy(order.orderDateTime.desc())
                .distinct();

        JPQLQuery<Order> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Order> fetchResults =  pageableQuery.fetchResults();


        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }

    @Override
    public Page<Order> accountCompleteDelivery(Pageable pageable, Account account) {
        QOrder order = QOrder.order;
        QAccount qAccount = QAccount.account;

        JPQLQuery<Order>  query =
                from(order)
                        .join(order.account, qAccount)
                        .where(
                                order.delivery.status.eq(DeliveryStatus.COMP)
                        )
                        .orderBy(order.orderDateTime.desc())
                        .distinct();

        JPQLQuery<Order> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Order> fetchResults =  pageableQuery.fetchResults();


        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }

    @Override
    public Page<Order> OrderDeliveryList(Pageable pageable) {

        QOrder order = QOrder.order;
        QAccount account = QAccount.account;

        JPQLQuery<Order>  query =
                from(order)
                        .join(order.account, account)
                        .where(
                                order.delivery.status.eq(DeliveryStatus.READY)
                        )
                        .distinct();

        JPQLQuery<Order> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Order> fetchResults =  pageableQuery.fetchResults();


        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());

    }

    @Override
    public Page<Order> OrderCancelList(Pageable pageable) {
        QOrder order = QOrder.order;
        QAccount account = QAccount.account;

        JPQLQuery<Order>  query =
                from(order)
                        .join(order.account, account)
                        .where(
                                order.status.eq(OrderStatus.CANCEL)
                        )
                        .distinct();

        JPQLQuery<Order> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Order> fetchResults =  pageableQuery.fetchResults();


        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }

    @Override
    public Page<Order> OrderCompleteList(Pageable pageable) {
        QOrder order = QOrder.order;
        QAccount account = QAccount.account;

        JPQLQuery<Order>  query =
                from(order)
                        .join(order.account, account)
                        .where(
                                order.delivery.status.eq(DeliveryStatus.COMP)
                        )
                        .distinct();

        JPQLQuery<Order> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Order> fetchResults =  pageableQuery.fetchResults();


        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }

    @Override
    public Page<Order> orderDeliveryList(Pageable pageable, Account account) {
        QOrder order = QOrder.order;
        QAccount qAccount = QAccount.account;

        JPQLQuery<Order>  query =
                from(order)
                        .join(order.account, qAccount)
                        .where(
                                order.delivery.status.eq(DeliveryStatus.READY)
                        )
                        .distinct();

        JPQLQuery<Order> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Order> fetchResults =  pageableQuery.fetchResults();


        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }


}

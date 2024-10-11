package projlee.modules.reservation.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import projlee.modules.reservation.domain.DogReservation;
import projlee.modules.reservation.domain.QDogReservation;


public class DogReservationRepositoryImpl extends QuerydslRepositorySupport implements DogReservationRepositoryCustom {

    public DogReservationRepositoryImpl() {
        super(DogReservation.class);
    }

    @Override
    public Page<DogReservation> findAll(Pageable pageable) {

        QDogReservation dogReservation = QDogReservation.dogReservation;

        JPQLQuery<DogReservation> query = from(dogReservation)
                                          .distinct();

        JPQLQuery<DogReservation> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<DogReservation> fetchResults = pageableQuery.fetchResults();

        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }


}

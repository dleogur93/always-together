package projlee.modules.dog.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import projlee.modules.dog.domain.Dog;
import projlee.modules.dog.domain.QDog;

import java.util.List;

public class DogRepositoryImpl extends QuerydslRepositorySupport implements DogRepositoryCustom{


    public DogRepositoryImpl() {
        super(Dog.class);
    }

    @Override
    public Page<Dog> findAllByAdoptionFalse(Pageable pageable) {
        QDog dog = QDog.dog;

        BooleanExpression adoptionFalse = dog.adoption.isFalse();


        JPQLQuery<Dog> query = from(dog)
                .leftJoin(dog.dogReservation).fetchJoin()  // ✅ Fetch Join 적용
                .where(adoptionFalse)
                .distinct();

        JPQLQuery<Dog> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Dog> fetchResults = pageableQuery.fetchResults();

        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());

    }


    @Override
    public List<String> findAllAvailableDogBreeds() {
        QDog dog = QDog.dog;

        JPQLQuery<String> query = from(dog)
                .select(dog.dogBreed)
                .where(dog.adoption.isFalse())
                .distinct();

        return query.fetch();
    }
}



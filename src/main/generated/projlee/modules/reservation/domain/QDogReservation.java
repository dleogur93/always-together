package projlee.modules.reservation.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDogReservation is a Querydsl query type for DogReservation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDogReservation extends EntityPathBase<DogReservation> {

    private static final long serialVersionUID = 768963730L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDogReservation dogReservation = new QDogReservation("dogReservation");

    public final projlee.modules.account.domain.QAccount account;

    public final projlee.modules.dog.domain.QDog dog;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath reservation = createBoolean("reservation");

    public final DatePath<java.time.LocalDate> reservationCreateTime = createDate("reservationCreateTime", java.time.LocalDate.class);

    public final DateTimePath<java.time.LocalDateTime> reservationTime = createDateTime("reservationTime", java.time.LocalDateTime.class);

    public QDogReservation(String variable) {
        this(DogReservation.class, forVariable(variable), INITS);
    }

    public QDogReservation(Path<? extends DogReservation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDogReservation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDogReservation(PathMetadata metadata, PathInits inits) {
        this(DogReservation.class, metadata, inits);
    }

    public QDogReservation(Class<? extends DogReservation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new projlee.modules.account.domain.QAccount(forProperty("account"), inits.get("account")) : null;
        this.dog = inits.isInitialized("dog") ? new projlee.modules.dog.domain.QDog(forProperty("dog"), inits.get("dog")) : null;
    }

}


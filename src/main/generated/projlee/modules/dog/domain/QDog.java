package projlee.modules.dog.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDog is a Querydsl query type for Dog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDog extends EntityPathBase<Dog> {

    private static final long serialVersionUID = 776038762L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDog dog = new QDog("dog");

    public final StringPath adopterName = createString("adopterName");

    public final BooleanPath adoption = createBoolean("adoption");

    public final NumberPath<Integer> dogAge = createNumber("dogAge", Integer.class);

    public final StringPath dogBreed = createString("dogBreed");

    public final StringPath dogName = createString("dogName");

    public final StringPath dogPicture = createString("dogPicture");

    public final DatePath<java.time.LocalDate> dogRegistrationDate = createDate("dogRegistrationDate", java.time.LocalDate.class);

    public final projlee.modules.reservation.domain.QDogReservation dogReservation;

    public final EnumPath<Sex> dogSex = createEnum("dogSex", Sex.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath information = createString("information");

    public final BooleanPath reservation = createBoolean("reservation");

    public QDog(String variable) {
        this(Dog.class, forVariable(variable), INITS);
    }

    public QDog(Path<? extends Dog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDog(PathMetadata metadata, PathInits inits) {
        this(Dog.class, metadata, inits);
    }

    public QDog(Class<? extends Dog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dogReservation = inits.isInitialized("dogReservation") ? new projlee.modules.reservation.domain.QDogReservation(forProperty("dogReservation"), inits.get("dogReservation")) : null;
    }

}


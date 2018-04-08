package dsow.besthackentrancetask.Database;

import android.arch.persistence.room.*;
import java.util.List;
import dsow.besthackentrancetask.Member;

@Dao
public interface MemberDao {

    @Query("SELECT * FROM member")
    List<Member> getAll();

    @Query("SELECT * FROM member WHERE _id= :id")
    Member getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Member member);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Member> member);

    @Update
    void update(Member member);

    @Delete
    void delete(Member member);

}

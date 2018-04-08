package dsow.besthackentrancetask;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Member implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int _id;

    public String name;
    public String surname;
    public String patronymic;
    public String role; //role in group
    public String group;
    public String about_me;
    public String link; //account link

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] image; //memeber's photo in bytes
}

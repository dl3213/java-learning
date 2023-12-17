package code.sibyl.reactive.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @Classname User
 * @Description TODO
 * @Date 2021/7/27 22:31
 * @Created by dyingleaf3213
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table("t_sys_user")
public class User implements Persistable<String> {
    @Id
    private String id;
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    @Transient
    public boolean isNew() {
        return newFlag || id == null;
    }

    @Transient
    private boolean newFlag;
    public User setNewFlag() {
        newFlag = true;
        return this;
    }
}

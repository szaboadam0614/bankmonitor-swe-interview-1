package bankmonitor.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Slf4j
public class Transaction {

    public static final String REFERENCE_KEY = "reference";
    public static final String AMOUNT_KEY = "amount";

    @Id
    @SequenceGenerator(name = "transaction_generator", sequenceName = "transaction_seq", allocationSize = 1)
    @GeneratedValue(generator = "transaction_generator", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime timestamp;

    private String data;

    public Transaction(String jsonData) {
        this.timestamp = LocalDateTime.now();
        this.data = jsonData;
    }

    public Integer getAmount() {
        JSONObject jsonData = new JSONObject(this.data);
        if (jsonData.has(AMOUNT_KEY)) {
            return jsonData.getInt(AMOUNT_KEY);
        }
        log.warn("Could not found amount in the given data: '{}'", data);
        return -1;
    }

    public String getReference() {
        JSONObject jsonData = new JSONObject(this.data);
        if (jsonData.has(REFERENCE_KEY)) {
            return jsonData.getString(REFERENCE_KEY);
        }
        log.warn("Could not found reference in the given data: '{}'", data);
        return "";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Transaction that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
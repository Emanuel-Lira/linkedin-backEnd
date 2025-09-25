package br.com.example.people_graph_service.domain;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import java.util.Objects;

@Node("User")
public class UserNode {

    @Id
    @Property("id")
    private Long id;
    private String name;

    public UserNode() {}

    public UserNode(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNode userNode = (UserNode) o;
        return Objects.equals(id, userNode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
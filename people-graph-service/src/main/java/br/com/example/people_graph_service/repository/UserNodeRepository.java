package br.com.example.people_graph_service.repository;

import br.com.example.people_graph_service.domain.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserNodeRepository extends Neo4jRepository<UserNode, Long> {
    @Query("MATCH (follower:User {id: $followerId}), (followed:User {id: $followedId}) CREATE (follower)-[:CONNECTED_TO]->(followed)")
    void userConnectsToUser(Long followerId, Long followedId);

    @Query("MATCH (u:User {id: $userId}), (c:Company {id: $companyId}) CREATE (u)-[:FOLLOWS]->(c)")
    void userFollowsCompany(Long userId, Long companyId);

    @Query("MATCH (u:User {id: $userId}), (c:Company {id: $companyId}) CREATE (u)-[:WORKED_AT]->(c)")
    void userWorkedAtCompany(Long userId, Long companyId);

    @Query("MATCH (u:User {id: $userId})-[:CONNECTED_TO]->(:User)-[:CONNECTED_TO]->(suggestion:User) " +
            "WHERE NOT (u)-[:CONNECTED_TO]->(suggestion) AND u <> suggestion " +
            "RETURN DISTINCT suggestion")
    List<UserNode> findSecondDegreeConnections(Long userId);
}
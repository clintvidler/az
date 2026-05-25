package az.server;

public record TaskResponse(
        Long id,
        String title,
        Long user_id
) {}

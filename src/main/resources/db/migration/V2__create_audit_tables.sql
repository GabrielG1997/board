CREATE TABLE a_boards (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    board_id BIGINT,  -- Não é PK nem UNIQUE, pois pode ter múltiplos registros
    name VARCHAR(255) NOT NULL,
    create_dt DATETIME NOT NULL,
    update_dt DATETIME
);

CREATE TABLE a_board_columns (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    column_board_id BIGINT,  -- Histórico da coluna
    board_id BIGINT,
    name VARCHAR(255) NOT NULL,
    board_order INT NOT NULL,
    type CHAR(1) NOT NULL,
    create_dt DATETIME NOT NULL,
    update_dt DATETIME,
    FOREIGN KEY (board_id) REFERENCES a_boards(id) ON DELETE NO ACTION  -- Referencia 'id' e não 'board_id'
);

CREATE TABLE a_cards (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    card_id BIGINT,  -- Histórico do card
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    create_dt DATETIME NOT NULL,
    update_dt DATETIME,
    is_blocked BIGINT NOT NULL,
    blocked_reason VARCHAR(255),
    unblocked_reason VARCHAR(255),
    last_blocked_dt DATETIME,
    last_unblocked_dt DATETIME,
    last_movement_dt DATETIME,
    column_board_id BIGINT,
    FOREIGN KEY (column_board_id) REFERENCES a_board_columns(id) ON DELETE NO ACTION  -- Referencia 'id' e não 'column_board_id'
);
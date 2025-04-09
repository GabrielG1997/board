-- Audit Triggers board table
CREATE TRIGGER trg_audit_insert_boards
ON boards
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO a_boards (board_id, name, create_dt, update_dt)
    SELECT i.board_id, i.name, GETDATE(), NULL
    FROM inserted i;
END;

GO
CREATE TRIGGER trg_audit_update_boards
ON boards
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO a_boards (board_id, name, create_dt, update_dt)
    SELECT i.board_id, i.name,
           (SELECT TOP 1 create_dt FROM a_boards WHERE board_id = i.board_id ORDER BY id DESC),
           GETDATE()
    FROM inserted i;
END;

GO
CREATE TRIGGER trg_audit_delete_boards
ON boards
AFTER DELETE
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO a_boards (board_id, name, create_dt, update_dt)
    SELECT d.board_id, d.name,
           (SELECT TOP 1 create_dt FROM a_boards WHERE board_id = d.board_id ORDER BY id DESC),
           GETDATE()
    FROM deleted d;
END;

GO
-- Audit Triggers board_columns table
CREATE TRIGGER trg_audit_insert_board_columns
ON board_columns
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO a_board_columns (column_board_id, board_id, name, board_order, type, create_dt, update_dt)
    SELECT i.column_board_id, i.board_id, i.name, i.board_order, i.type, GETDATE(), NULL
    FROM inserted i;
END;

GO
CREATE TRIGGER trg_audit_update_board_columns
ON board_columns
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO a_board_columns (column_board_id, board_id, name, board_order, type, create_dt, update_dt)
    SELECT i.column_board_id, i.board_id, i.name, i.board_order, i.type,
           (SELECT TOP 1 create_dt FROM a_board_columns WHERE column_board_id = i.column_board_id ORDER BY id DESC),
           GETDATE()
    FROM inserted i;
END;


GO
CREATE TRIGGER trg_audit_delete_board_columns
ON board_columns
AFTER DELETE
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO a_board_columns (column_board_id, board_id, name, board_order, type, create_dt, update_dt)
    SELECT d.column_board_id, d.board_id, d.name, d.board_order, d.type,
           (SELECT TOP 1 create_dt FROM a_board_columns WHERE column_board_id = d.column_board_id ORDER BY id DESC),
           GETDATE()
    FROM deleted d;
END;

GO
-- Audit Triggers cards table
CREATE TRIGGER trg_audit_insert_cards
ON cards
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO a_cards (card_id, title, description, create_dt, update_dt, is_blocked, blocked_reason, unblocked_reason, last_blocked_dt, last_unblocked_dt, last_movement_dt, column_board_id)
    SELECT i.card_id, i.title, i.description, GETDATE(), NULL, i.is_blocked, i.blocked_reason, i.unblocked_reason, i.last_blocked_dt, i.last_unblocked_dt, i.last_movement_dt, i.column_board_id
    FROM inserted i;
END;

GO
CREATE TRIGGER trg_audit_update_cards
ON cards
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO a_cards (card_id, title, description, create_dt, update_dt, is_blocked, blocked_reason, unblocked_reason, last_blocked_dt, last_unblocked_dt, last_movement_dt, column_board_id)
    SELECT i.card_id, i.title, i.description,
            (SELECT TOP 1 create_dt FROM a_cards WHERE card_id = i.card_id ORDER BY id DESC),
            GETDATE(), i.is_blocked,
            CASE
                WHEN i.is_blocked = 0 AND unblocked_reason IS NOT NULL
                THEN (SELECT TOP 1 blocked_reason FROM a_cards WHERE card_id = i.card_id ORDER BY last_blocked_dt DESC)
                ELSE i.blocked_reason
            END AS blocked_reason,

            i.unblocked_reason,

            CASE
                WHEN i.is_blocked = 0 AND unblocked_reason IS NOT NULL
                THEN (SELECT TOP 1 last_blocked_dt FROM a_cards WHERE card_id = i.card_id ORDER BY last_blocked_dt DESC)
                ELSE i.last_blocked_dt
            END AS last_blocked_dt,
           i.last_unblocked_dt, i.last_movement_dt, i.column_board_id
    FROM inserted i;
END;

GO
CREATE TRIGGER trg_audit_delete_cards
ON cards
AFTER DELETE
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO a_cards (card_id, title, description, create_dt, update_dt, is_blocked, blocked_reason, unblocked_reason, last_blocked_dt, last_unblocked_dt, last_movement_dt, column_board_id)
    SELECT d.card_id, d.title, d.description,
           (SELECT TOP 1 create_dt FROM a_cards WHERE card_id = d.card_id ORDER BY id DESC),
           GETDATE(), d.is_blocked, d.blocked_reason, d.unblocked_reason, d.last_blocked_dt, d.last_unblocked_dt, d.last_movement_dt, d.column_board_id
    FROM deleted d;
END;
GO
let roleList = [];

getAllUsers();

function getAllUsers() {
    $.getJSON("http://localhost:8080/admin/allUsers", function (data) {
        let rows = '';
        // проходимся циклом по всем User
        $.each(data, function (key, user) {
            // всех полученных User добавляем в строку
            rows += createRows(user);
        });
        //строки добавляем в таблицу allUsers
        $('#tableAllUsers').append(rows);

        // получение списка ролей
        $.ajax({
            url: '/admin/allRoles',
            method: 'GET',
            dataType: 'json',
            success: function (role) {
                roleList = role;
            }
        });
    });
}

//Создаём строки
function createRows(user) {

    let user_rows = '<tr id=' + user.id + '>';
    user_rows += '<td>' + user.id + '</td>';
    user_rows += '<td>' + user.login + '</td>';
    user_rows += '<td>' + user.password + '</td>';
    user_rows += '<td>' + user.name + '</td>';
    user_rows += '<td>' + user.lastName + '</td>';
    user_rows += '<td>' + user.age + '</td>';
    user_rows += '<td>' + user.salary + '</td>';
    user_rows += '<td>';
    let roles = user.authorities;
    for (let role of roles) {
        user_rows += role.role + ' ';
    }
    user_rows += '</td>' +
        '<td>' + '<input id="btnEdit" value="Edit" type="button" ' +
        'class="btn-info btn edit-btn" data-toggle="modal" data-target="#editModal" ' +
        'data-id="' + user.id + '">' + '</td>' +

        '<td>' + '<input id="btnDelete" value="Delete" type="button" class="btn btn-danger del-btn" ' +
        'data-toggle="modal" data-target="#deleteModal" data-id=" ' + user.id + ' ">' + '</td>';
    user_rows += '</tr>';

    return user_rows;
}

// Получение списка всех ролей для отображения в модальном окне
function getUserRolesForEdit() {
    var allRoles = [];

    $.each($("select[name='editRoles'] option:selected"), function () {
        var role = {};
        role.id = $(this).attr('id');
        role.role = $(this).attr('role');
        allRoles.push(role);
        console.log("role: " + JSON.stringify(role));
    });
    return allRoles;
}

//Edit юзер
$(document).on('click', '.edit-btn', function () {

    const userId = $(this).attr('data-id');
    $.ajax({
        url: '/admin/' + userId,
        method: 'GET',
        dataType: 'json',
        success: function (user) {
            $('#editId').val(user.id);
            $('#editLogin').val(user.login);
            $('#editPassword').val(user.password);
            $('#editName').val(user.name);
            $('#editLastName').val(user.lastName);
            $('#editAge').val(user.age);
            $('#editSalary').val(user.salary);
            $('#editRole').empty();
            //Получаем текущую роль
            roleList.map(role => {
                let flag = user.authorities.find(item => item.id === role.id) ? 'selected' : '';
                $('#editRole').append('<option id="' + role.id + '" ' + flag + ' role="' + role.role + '" >' +
                    role.role + '</option>')
            })

        }
    });
});


$('#editButton').on('click', (e) => {
    e.preventDefault();

    let userEditId = $('#editId').val();

    var editUser = {
        id: $("input[name='id']").val(),
        login: $("input[name='login']").val(),
        name: $("input[name='name']").val(),
        lastName: $("input[name='lastName']").val(),
        age: $("input[name='age']").val(),
        password: $("input[name='password']").val(),
        salary: $("input[name='salary']").val(),
        role: getUserRolesForEdit()
    }

    $.ajax({
        url: '/admin',
        method: 'PUT',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: JSON.stringify(editUser),

        success: (data) => {
            let newRow = createRows(data);
            console.log("newRow: " + newRow)
            $('#tableAllUsers').find('#' + userEditId).replaceWith(newRow);
            $('#editModal').modal('hide');
            $('#admin-tab').tab('show');
        },
    });
});

//Удаляем юзера
$(document).on('click', '.del-btn', function () {

    // Получение ID юзера для последующего удаления
    let userId = $(this).attr('data-id');

    $.ajax({
        url: '/admin/' + userId,
        method: 'GET',
        dataType: 'json',
        success: function (user) {
            $('#deleteId').empty().val(user.id);
            $('#deleteLogin').empty().val(user.login);
            $('#deleteName').empty().val(user.name);
            $('#deleteLastName').empty().val(user.lastName);
            $('#deleteAge').empty().val(user.age);
            $('#deletePassword').empty().val(user.password);
            $('#deleteSalary').empty().val(user.salary);
            $('#deleteRole').empty();
            //Получаем текущую роль
            roleList.map(role => {
                let flag = user.authorities.find(item => item.id === role.id) ? 'selected' : '';
                $('#deleteRole').append('<option id="' + role.id + '" ' + flag + ' role="' + role.role + '" >' +
                    role.role + '</option>')
            })
        }
    });
});

//Удаление юзера (кнопка)
$('#deleteButton').on('click', (e) => {

    e.preventDefault();
    let userId = $('#deleteId').val();
    $.ajax({
        url: '/admin/' + userId,
        method: 'DELETE',
        success: function () {
            $('#' + userId).remove();
            $('#deleteModal').modal('hide');
            $('#admin-tab').tab('show');
        },
    });
});

//Получение ролей для addUser
function getUserRolesForAdd() {
    var allRoles = [];

    $.each($("select[name='addRoles'] option:selected"), function () {
        var role = {};
        role.id = $(this).attr('id');
        role.role = $(this).attr('name');
        allRoles.push(role);
        console.log("role: " + JSON.stringify(role));
    });
    return allRoles;
}

//Вкладка новый юзер
$('.newUser').on('click', () => {

    $('#login').empty().val('')
    $('#password').empty().val('')
    $('#name').empty().val('')
    $('#lastName').empty().val('')
    $('#age').empty().val('')
    $('#salary').empty().val('')
    $('#addRole').empty().val('')
    roleList.map(role => {
        $('#addRole').append('<option id="' + role.id + '" role="' + role.role + '">' +
            role.role + '</option>')
    })
})

//Юзер добавляется
$("#addNewUserButton").on('click', () => {

    let newUser = {
        login: $('#login').val(),
        password: $('#password').val(),
        name: $('#name').val(),
        lastName: $('#lastName').val(),
        age: $('#age').val(),
        salary: $('#salary').val(),
        role: getUserRolesForAdd()
    }

    $.ajax({
        url: 'http://localhost:8080/admin',
        method: 'POST',
        dataType: 'json',
        data: JSON.stringify(newUser),
        contentType: 'application/json; charset=utf-8',
        success: function () {
            $('#tableAllUsers').empty();
            getAllUsers();
            $('#admin-tab').tab('show');
        },
    });
});
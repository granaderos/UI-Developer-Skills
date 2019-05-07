var baseAPIURL = "http://localhost:8080/api-developers/rest";

$(document).ready(function() {
    $("#formTabs").tabs();
    $("#tableTabs").tabs();
    $("#birthDate").datepicker({ dateFormat: 'yy-mm-dd' });
    $("#btnCancelUpdate").hide();

    $("#formSkill").submit(function(e) {
        e.preventDefault();
        var skill = $("#skill").val();
        var skillData = {
            "skill": skill
        }
        $.ajax({
            type: "POST",
            url: baseAPIURL + "/skills/add",
            data: JSON.stringify(skillData),
            contentType: "application/json",
            success: function(data) {
                $("#skill").val("");
                displaySkills();
                alert(data);

            },
            error: function(data) {
                console.log("Error in adding skill " + JSON.stringify(data));
            }
        });
    });

    $("#formDeveloper").submit(function(e) {
        e.preventDefault();
        var firstName = $("#firstName").val();
        var middleName = $("#middleName").val();
        var lastName = $("#lastName").val();
        var birthDate = $("#birthDate").val();
        var position = $("#position").val();

        var devData = {
            "firstName": firstName,
            "middleName": middleName,
            "lastName": lastName,
            "birthDate": birthDate,
            "position": position
        }
        $.ajax({
            type: "POST",
            url: baseAPIURL + "/developers/add",
            data: JSON.stringify(devData),
            contentType: "application/json",
            success: function(data) {
                displayDevelopers();
                alert(data);

                $("#firstName").val("");
                $("#middleName").val("");
                $("#lastName").val("");
                $("#birthDate").val("");
                $("#position").val("");
            },
            error: function(data) {
                console.log("Error in adding a developer " + JSON.stringify(data));
            }
        });
    });

    $("#divAddSkillAssessment").submit(function(e) {
        e.preventDefault();

        if($("#btnSubmitSkillAssessment").html() == "Add") {
            var skillId = $("#saSkill").val();
            var devId = $("#saDeveloper").val();
            var monthsOfExp = $("#saMonthsOfExperience").val();
            var skillLevel = $("#saSkillLevel").val();

            var saData = {
                "skill": { "skillId": skillId },
                "developer": { "developerId": devId },
                "monthsOfExperience": monthsOfExp,
                "skillLevel": skillLevel
            }
            $.ajax({
                type: "POST",
                url: baseAPIURL + "/skillAssessments/add",
                data: JSON.stringify(saData),
                contentType: "application/json",
                success: function(data) {
                    $("#saSkill").val("");
                    $("#saDeveloper").val("");
                    $("#saMonthsOfExperience").val(null);
                    $("#saSkillLevel").val("");

                    displaySkillCapabilityReport();
                    displayDevelopers();
                    alert(data);

                },
                error: function(data) {
                    console.log("Error in adding skill assessment " + JSON.stringify(data));
                }
            });
        } else {
            // update here
            
            var skillId = $("#saSkill").val();
            var devId = $("#saDeveloper").val();
            var monthsOfExp = $("#saMonthsOfExperience").val();
            var skillLevel = $("#saSkillLevel").val();

            var saData = {
                "skill": { "skillId": skillId },
                "developer": { "developerId": devId },
                "monthsOfExperience": monthsOfExp,
                "skillLevel": skillLevel
            }
            $.ajax({
                type: "PUT",
                url: baseAPIURL + "/skillAssessments/update",
                data: JSON.stringify(saData),
                contentType: "application/json",
                success: function(data) {
                    $("#saSkill").val("");
                    $("#saDeveloper").val("");
                    $("#saMonthsOfExperience").val(null);
                    $("#saSkillLevel").val("");

                    $("#btnSubmitSkillAssessment").html("Add");
                    $("#h3SkillAssessment").html("Add Skill Assessment");
                    $("#btnCancelUpdate").hide();

                    $("#saDeveloper").prop('disabled', false);
                    $("#saSkill").prop('disabled', false);

                    displaySkillCapabilityReport();
                    displayDevelopers();
                    alert(data);

                },
                error: function(data) {
                    console.log("Error in adding skill assessment " + JSON.stringify(data));
                }
            });
        }
    });

    $("#formSearchDev").submit(function(e) {
        e.preventDefault();
        var skill = $("#sSkill").val();
        var skillLevel = $("#sSkillLevel").val();
        var monthsOfExperience = $("#sMonthsOfExperience").val();
        var firstName = $("#sFirstName").val();
        var lastName = $("#sLastName").val();

        var queryParam = "";
        if(skill.trim().length > 0) queryParam += "skill="+skill;
        if(skillLevel.trim().length > 0) queryParam += "skillLevel="+skillLevel;
        if(monthsOfExperience.trim().length > 0) queryParam += "monthsOfExperience="+monthsOfExperience;
        if(firstName.trim().length > 0) queryParam += "firstName="+firstName;
        if(lastName.trim().length > 0) queryParam += "lastName="+lastName;

        if(queryParam != "") {
            $.ajax({
                type: "GET",
                url: baseAPIURL + "/developers/search?"+queryParam,
                success: function(data) {
                    $("#tbodyDevelopers").html("");
                    displayDeveloperData(data);
                },
                error: function(data) {
                    console.log("Error in searching developer " + JSON.stringify(data));
                }
            });
        } else {
            displayDevelopers();
        }
    });

    $("#btnCancelUpdate").click(function() {
        $("#btnSubmitSkillAssessment").html("Add");
        $("#h3SkillAssessment").html("Add Skill Assessment");
        $("#btnCancelUpdate").hide();

        $("#saDeveloper").prop('disabled', false);
        $("#saSkill").prop('disabled', false);
    });

    displaySkills();
    displayDevelopers();
    displaySkillLevels();
    displaySkillCapabilityReport();
});

function displayDeveloperData(data) {
    for(var i = 0; i < data.length; i++) {
        var dev = data[i].developer;
        var skills = data[i].skills;
        var rowspan = skills.length;
        console.log("rowspan = " + rowspan);
        if(rowspan <= 0) rowspan = 1;
        else if(rowspan > 1) rowspan++;
        var row = "<tr>"
            + "<td valign='middle' rowspan="+rowspan+">"+dev.developerId+"</td>"
            + "<td valign='middle' rowspan="+rowspan+">"+dev.firstName + " " + dev.middleName + " " + dev.lastName + "</td>"
            + "<td valign='middle' rowspan="+rowspan+">"+dev.birthDate+"</td>"
            + "<td valign='middle' rowspan="+rowspan+">"+dev.position+"</td>";
        if(skills.length == 1) {
            row += "<td>"+skills[0].skill+"</td>" +
                "<td>"+skills[0].skillLevel+"</td>" +
                "<td>"+skills[0].monthsOfExperience+"</td>"+
                "<td><button class='btn btn-xs' onclick=\"updateSkillAssessment("+dev.developerId+", "+skills[0].skillId+", '"+skills[0].skillLevel+"', "+skills[0].monthsOfExperience+")\">Update Skill Assessment</button></td></tr>";
        } else if(skills.length > 1) {
            row += "</tr>";
            for(var j = 0; j < skills.length; j++) {
                row += "<tr>" +
                        "<td>"+skills[j].skill+"</td>" +
                        "<td>"+skills[j].skillLevel+"</td>" +
                        "<td>"+skills[j].monthsOfExperience+"</td>"+
                        "<td><button class='btn btn-xs' onclick=\"updateSkillAssessment("+dev.developerId+", "+skills[j].skillId+", '"+skills[j].skillLevel+"', "+skills[j].monthsOfExperience+")\">Update Skill Assessment</button></td>" +
                    "</tr>";
            }
        } else {
            row += "<td colspan=3 style='text-align: center;'>Not specified.</td><td><button onclick=\"addASkillToDev("+dev.developerId+")\" class='btn btn-xs btn-primary'>Add a Skill</button></td></tr>";
        }
        
        $("#tbodyDevelopers").append(row);
        $("#saDeveloper").append("<option value="+dev.developerId+">"+dev.firstName+" "+ dev.middleName+" "+dev.lastName+"</option>");

    }
}

function updateSkillAssessment(devId, skillId, skillLevel, monthsOfExperience) {
    $("#saDeveloper").val(devId);
    $("#saDeveloper").prop('disabled', 'disabled');
    $("#saSkill").val(skillId);
    $("#saSkill").prop('disabled', 'disabled');
    $("#saSkillLevel").val(skillLevel);
    $("#saMonthsOfExperiece").val(monthsOfExperience);

    $("#btnSubmitSkillAssessment").html("Save");
    $("#h3SkillAssessment").html("Update Skill Assessment");
    $("#btnCancelUpdate").show();



    $("#aAssessmentForm").click();
    $("#saSkillLevel").focus();
}

function addASkillToDev(dev) {
    $("#aAssessmentForm").click();
    $("#saDeveloper").focus();
    $("#saDeveloper").val(dev)
}

function displaySkillCapabilityReport() {
    $.ajax({
        type: "GET",
        url: baseAPIURL + "/skills/capabilityReport",
        success: function(data) {
            var reports = "";
            for(var i = 0; i < data.length; i++) {
                var rows = "";
                var row = data[i];
                rows += "<tr>"
                        + "<td>"+row.skill+"</td>";

                for(var j = 0; j < row.report.length; j++) {
                    rows += "<td>"+row.report[j].count+"</td>";
                }
                rows += "<td>"+row.total+"</td></tr>";
                reports += rows;
            }
            $("#tbodySkillCapabilityReport").html(reports);
        },
        error: function(data) {
            console.log("Error in adding skill " + JSON.stringify(data));
        }
    });
}

function displaySkills() {
    // display skills
    $.ajax({
        type: "GET",
        url: baseAPIURL + "/skills",
        success: function(data) {
            var skills = data;
            $("#saSkill").html("");
            $("#tbodySkills").html("");
            for(var i = 0; i < skills.length; i++) {
                $("#tbodySkills").append("<tr><td>"+skills[i].skill+"</td></tr>");
                $("#saSkill").append("<option value="+skills[i].skillId+">"+skills[i].skill+"</option>");

            }
        },
        error: function(data) {
            console.log("Error in adding skill " + JSON.stringify(data));
        }
    });
}

function displayDevelopers() {
    $.ajax({
        type: "GET",
        url: baseAPIURL + "/developers",
        success: function(data) { 
            $("#saDeveloper").html("");
            $("#tbodyDevelopers").html("");
            displayDeveloperData(data);
        },
        error: function(data) {
            console.log("Error in adding skill " + JSON.stringify(data));
        }
    });

}

function displaySkillLevels() {
    $.ajax({
        type: "GET",
        url: baseAPIURL + "/skills/levels",
        success: function(data) {
            var levels = data;
            $("#saSkillLevel").html("");
            var thead = "<tr><th>Skill</th>"
            for(var i = 0; i < levels.length; i++) {
                $("#saSkillLevel").append("<option value="+levels[i]+">"+levels[i]+"</option>");
                // thead += "<th>"+i+":"+levels[i]+"</th>";
                thead += "<th>"+levels[i]+"</th>";
            }
            thead += "<th>Total</th></tr>";
            $("#theadSkillCapabilityReport").html(thead);
        },
        error: function(data) {
            console.log("Error in adding skill " + JSON.stringify(data));
        }
    });
}
package com.thinktech.service.database;

import com.thinktech.model.assemblers.QuestionnaireAssembler;
import com.thinktech.model.domain.Questionnaire;
import com.thinktech.service.CarbonUtilities;

import java.sql.CallableStatement;

public class UserDataProvider extends CarbonDataProvider {

    public void AddUser(String userId, Questionnaire questionnaire) throws Exception {
        try {
            connection = this.OpenConnection();

            String diet = CarbonUtilities.ConvertUpperCaseToCamelCase(questionnaire.getDiet().toString());
            String carUsage = CarbonUtilities.ConvertUpperCaseToCamelCase(questionnaire.getCarUsage().toString());
            String housingType = CarbonUtilities.ConvertUpperCaseToCamelCase(questionnaire.getHouseType().toString());
            String housingAge = QuestionnaireAssembler.DisassembleHouseAge(questionnaire.getHouseAge());

            callableStatement = connection.prepareCall("{CALL ADD_USER(?,?,?,?,?,?,?,?)}");
            callableStatement.setString(1, userId);
            callableStatement.setString(2, diet);
            callableStatement.setString(3, carUsage);
            callableStatement.setInt(4, questionnaire.getCarMileageMiles());
            callableStatement.setInt(5, questionnaire.getNumberInHousehold());
            callableStatement.setString(6, housingType);
            callableStatement.setString(7, housingAge);

        } catch (Exception e) {
            String message = String.format("Unable to add user to database %s", userId);
            throw new Exception(message, e);
        } finally {
            CloseConnection();
        }
    }

    public boolean CheckUserExists(String userId) throws Exception {
        try {
            connection = this.OpenConnection();
            preparedStatement = connection.prepareStatement("select count(*) count from carbon.users where auth_user_id = ?");

            preparedStatement.setString(1, userId);
            resultSet = preparedStatement.executeQuery();
            resultSet.first();
            int count = resultSet.getInt("count");
            return count != 0;

        } catch (Exception e) {
            String message = String.format("Unable to query database to check user %s", userId);
            throw new Exception(message, e);
        } finally {
            CloseConnection();
        }

    }

}

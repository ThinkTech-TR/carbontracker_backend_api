package com.thinktech.service.database;

import com.thinktech.model.assemblers.QuestionnaireAssembler;
import com.thinktech.model.domain.Questionnaire;
import com.thinktech.model.enums.CarUsage;
import com.thinktech.service.CarbonUtilities;

import java.sql.CallableStatement;

public class UserDataProvider extends CarbonDataProvider {

    public void AddUpdateUser(String userId, Questionnaire questionnaire) throws Exception {
        try {
            connection = this.OpenConnection();

            String userCategory = CarbonUtilities.ConvertUpperCaseToCamelCase(questionnaire.getUserCategory().toString());
            String diet = CarbonUtilities.ConvertUpperCaseToCamelCase(questionnaire.getDiet().toString());
            String carUsage = CarbonUtilities.ConvertUpperCaseToCamelCase(questionnaire.getCarUsage().toString());
            String housingType = CarbonUtilities.ConvertUpperCaseToCamelCase(questionnaire.getHouseType().toString());
            String housingAge = QuestionnaireAssembler.DisassembleHouseAge(questionnaire.getHouseAge());

            callableStatement = connection.prepareCall("{CALL ADD_UPDATE_USER(?,?,?,?,?,?,?,?)}");
            callableStatement.setString(1, userId);
            callableStatement.setString(2, userCategory);
            callableStatement.setString(3, diet);
            if (questionnaire.getCarUsage() == CarUsage.NO_CAR) {
                callableStatement.setNull(4, java.sql.Types.NVARCHAR);
            } else {
                callableStatement.setString(4, carUsage);
            }
            callableStatement.setString(4, carUsage);
            callableStatement.setInt(5, questionnaire.getCarMileageMiles());
            callableStatement.setInt(6, questionnaire.getNumberInHousehold());
            callableStatement.setString(7, housingType);
            callableStatement.setString(8, housingAge);

            callableStatement.executeUpdate();

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

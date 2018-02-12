package com.vitalityactive.va.profile;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dto.PersonalDetailsDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

public class PersonalDetailsPresenterImpl extends BasePersonalDetailsPresenterImpl {

    public PersonalDetailsPresenterImpl(EventDispatcher eventDispatcher, DateFormattingUtilities dateFormatUtil, ProfileImageProvider profileImageProvider,
                                        PartyInformationRepository partyInfoRepo, PersonalDetailsInteractor personalDetailsInteractor, AppConfigRepository appConfigRepository) {

        super(eventDispatcher, dateFormatUtil, profileImageProvider, partyInfoRepo, personalDetailsInteractor, appConfigRepository);
    }

    @Override
    protected LocalDate getDateOfBirth(PersonalDetailsDTO personalDetails){
        return LocalDate.create(personalDetails.getDateOfBirthString());
    }

    @Override
    protected String getDateOfBirthWithFormat(LocalDate dateOfBirth){
        return dateFormatUtil.getFormattedDate("yyyy/MM/dd", dateOfBirth);
    }
}
